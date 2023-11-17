package org.binaracademy.challenge_7.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.binaracademy.challenge_7.entity.Order;
import org.binaracademy.challenge_7.entity.OrderDetail;
import org.binaracademy.challenge_7.entity.Product;
import org.binaracademy.challenge_7.entity.User;
import org.binaracademy.challenge_7.entity.response.OrderDetailResponse;
import org.binaracademy.challenge_7.entity.response.OrderResponse;
import org.binaracademy.challenge_7.entity.response.Response;
import org.binaracademy.challenge_7.repository.OrderDetailRepository;
import org.binaracademy.challenge_7.repository.OrderRepository;
import org.binaracademy.challenge_7.repository.ProductRepository;
import org.binaracademy.challenge_7.repository.UserRepository;
import org.binaracademy.challenge_7.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ExecutorService executorService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, OrderDetailRepository orderDetailRepository, ExecutorService executorService){
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.executorService = executorService;
    }

    @Transactional
    @Override
    public Response<String> createOrder(Long productCode, Integer quantity) {
        Response<String> response = new Response<>();
        try {
            User userContext = userRepository.findByUsername(getUsernameContext());
            Product product = productRepository.findById(productCode).orElse(null);
            if (product != null){
                Order userOrder = orderRepository.getDataOrderFalseById(userContext.getId());
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProduct(product);
                orderDetail.setOrder(userOrder);
                orderDetail.setQuantity(quantity);
                orderDetail.setTotalPrice(((long) quantity * product.getPrice()));
                if (userOrder != null){
                    userOrder.getOrderDetails().add(orderDetail);
                    userContext.getOrders().add(userOrder);
                    userRepository.save(userContext);
                    response.setError(false);
                    response.setMessage("Success");
                    response.setMessage("Berhasil membuat order: "+ product.getName() + " dengan qty: "+quantity);
                }else {
                    userOrder = new Order();

                    orderDetail.setOrder(userOrder);
                    userOrder.setUser(userContext);
                    userOrder.setDestination(userContext.getAddress());
                    userOrder.setTime(LocalDate.now());
                    userOrder.setIsCompleted(false);
                    userOrder.setOrderDetails(new ArrayList<>());

                    userOrder.getOrderDetails().add(orderDetail);
                    userContext.getOrders().add(userOrder);
                    userRepository.save(userContext);
                    response.setError(false);
                    response.setMessage("Success");
                    response.setMessage("Berhasil membuat order: "+ product.getName() + " dengan qty: "+quantity);
                }
            }else {
                response.setError(true);
                response.setMessage("Failed");
                response.setData("Gagal membuat order. Produk yang dipilih tidak tersedia");
            }
        }catch (Exception e){
            response.setError(true);
            response.setMessage("Failed");
            response.setData("Gagal membuat order");
        }
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public Response<OrderResponse> currentOrder() {
        Response<OrderResponse> response = new Response<>();
        try {
            User userContext = userRepository.findByUsername(getUsernameContext());
            CompletableFuture<OrderResponse> orderResponseCompletableFuture = CompletableFuture.supplyAsync(() -> orderRepository.getDataCurrentOrder(userContext.getId()), executorService);
            CompletableFuture<List<OrderDetailResponse>> orderDetailResponseCompletableFuture = CompletableFuture.supplyAsync(() -> orderDetailRepository.getListOrderDetail(userContext.getId(), false), executorService);

            OrderResponse orderResponse= orderResponseCompletableFuture.get();
            List<OrderDetailResponse> orderDetailResponse = orderDetailResponseCompletableFuture.get();
            if (orderResponse != null){
                orderResponse.setOrderDetailResponses(orderDetailResponse);
                response.setError(false);
                response.setMessage("Success");
                response.setData(orderResponse);
            }else {
                response.setError(false);
                response.setMessage("Anda tidak memiliki order. Silahkan order silahkan order terlebih dahulu");
                response.setData(null);
            }
        }catch (Exception e){
            response.setError(true);
            response.setMessage("Terjadi kesalahan");
            response.setData(null);
        }
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public Response<List<OrderResponse>> historyOrder() {
        Response<List<OrderResponse>> response = new Response<>();
        try {
            User userContext = userRepository.findByUsername(getUsernameContext());

            CompletableFuture<List<OrderResponse>> listOrderResponseFuture = CompletableFuture.supplyAsync(() -> orderRepository.getListHistoryOrder(userContext.getId()), executorService);
            CompletableFuture<List<OrderDetailResponse>> listOrderDetailResponse = CompletableFuture.supplyAsync(() -> orderDetailRepository.getListOrderDetail(userContext.getId(), true), executorService);

            List<OrderResponse> orderResponses = listOrderResponseFuture.get().stream().map(orderResponse -> {
                orderResponse.setOrderDetailResponses(new ArrayList<>());
                try {
                    listOrderDetailResponse.get().forEach(orderDetailResponse -> {
                        if (Objects.equals(orderResponse.getOrderId(), orderDetailResponse.getOrderId())){
                            orderResponse.getOrderDetailResponses().add(orderDetailResponse);
                        }
                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
                return orderResponse;
            }).collect(Collectors.toList());

            response.setError(false);
            response.setMessage("Success");
            response.setData(orderResponses);
        }catch (Exception e){
            response.setError(true);
            response.setMessage("Terjadi kesalahan");
            response.setData(null);
        }
        return response;
    }

    @Transactional
    @Override
    public byte[] makeOrder() {
        Integer sumQty = 0;
        Long sumPrice = 0L;
        try {
            User userContext = userRepository.findByUsername(getUsernameContext());
            Order order = orderRepository.findOrderByUser_IdAndIsCompleted(userContext.getId(), false);
            if (order != null){
                for (int i = 0; i < order.getOrderDetails().size(); i++){
                    sumQty += order.getOrderDetails().get(i).getQuantity();
                    sumPrice += order.getOrderDetails().get(i).getTotalPrice();
                }

                Integer finalSumQty = sumQty;
                Long finalSumPrice = sumPrice;
                CompletableFuture<List<OrderDetailResponse>> listOrderDetailResponse = CompletableFuture.supplyAsync(() -> order.getOrderDetails().stream().map(orderDetail -> {
                    OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
                    orderDetailResponse.setProductName(orderDetail.getProduct().getName());
                    orderDetailResponse.setQuantity(orderDetail.getQuantity());
                    orderDetailResponse.setTotalPrice(orderDetail.getTotalPrice());
                    return orderDetailResponse;
                }).collect(Collectors.toList()), executorService);

                CompletableFuture<Map<String, Object>> mapCompletableFuture = CompletableFuture.supplyAsync(() -> {
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("noPesanan", order.getId());
                    parameters.put("username", order.getUser().getUsername());
                    parameters.put("destination", order.getDestination());
                    parameters.put("totalQuantity", finalSumQty);
                    parameters.put("sumTotalPrice", finalSumPrice);

                    return parameters;
                }, executorService);

                JasperPrint jasperPrint = JasperFillManager.fillReport(
                        JasperCompileManager.compileReport(ResourceUtils.getFile("binarinvoice.jrxml").getAbsolutePath()),
                        mapCompletableFuture.get(),
                        new JRBeanCollectionDataSource(listOrderDetailResponse.get())
                );
                order.setIsCompleted(true);
                orderRepository.save(order);
                return JasperExportManager.exportReportToPdf(jasperPrint);
            }else {
                log.info("Order null");
                return new byte[0];
            }
        }catch (Exception e){
            log.error("Terjadi kesalahan: "+e.getMessage());
            return new byte[0];
        }
    }
    private String getUsernameContext(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            return authentication.getName();
        }else {
            return null;
        }
    }

}
