package com.company.tasks.online.store.ws.controllers;

import com.company.tasks.online.store.entities.Order;
import com.company.tasks.online.store.exceptions.BadRequestException;
import com.company.tasks.online.store.exceptions.DBValidationException;
import com.company.tasks.online.store.exceptions.EntityNotFoundException;
import com.company.tasks.online.store.services.OrderService;
import com.company.tasks.online.store.ws.dto.ErrorResponse;
import com.company.tasks.online.store.ws.dto.OrderCreateRequest;
import com.company.tasks.online.store.ws.dto.OrderUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;


@Slf4j
@RestController
@RequestMapping("/v1/orders")
@Validated
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;


    /**
     * GET request which returns list of orders
     *
     * @return list of {@link Order} orders
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getAllOrders() {
        try {
            log.debug("Getting orders from db");
            return ResponseEntity.ok(this.orderService.getAllOrders());
        } catch (Exception ex) {
            log.warn("Error while getting orders from db, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error while getting orders"));
        }
    }


    /**
     * GET request which returns order by id
     *
     * @return {@link Order} orders
     */
    @GetMapping(value = "/{order_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getOrderById(@PathVariable("order_id") @Positive(message = "orderId must be positive") int orderId) {
        try {
            log.debug("Getting order by id={} from db", orderId);
            return ResponseEntity.ok(this.orderService.getOrderById(orderId));
        } catch (EntityNotFoundException ex) {
            log.warn("Entity not found, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            log.warn("General error while processing request, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error while processing request"));
        }
    }


    /**
     * Post request which creates order
     *
     * @param orderCreateRequest - new order
     * @return created new {@link Order} order
     */
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderCreateRequest orderCreateRequest) {
        try {
            log.debug("Creating new order");
            Order savedOrder = this.orderService.saveOrder(orderCreateRequest.convertToOrderDto());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
        } catch (BadRequestException ex) {
            log.warn("Invalid request parameters, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
        } catch (EntityNotFoundException ex) {
            log.warn("Entity not found, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
        } catch (DBValidationException ex) {
            log.warn("Db validation failed, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
        } catch (Exception ex) {
            log.warn("General error while processing request, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error while creating product"));
        }
    }


    @PutMapping(value = "/{order_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateOrder(@PathVariable("order_id") @Positive(message = "orderId must be positive") int orderId,
                                         @Valid @RequestBody OrderUpdateRequest orderUpdateRequest) {
        try {
            log.debug("updating order");
            Order savedOrder = this.orderService.updateOrder(orderUpdateRequest.convertToOrderDto(orderId));
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
        } catch (BadRequestException ex) {
            log.warn("Invalid request parameters, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
        } catch (EntityNotFoundException ex) {
            log.warn("Entity not found, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
        } catch (DBValidationException ex) {
            log.warn("Db validation failed, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
        } catch (Exception ex) {
            log.warn("General error while processing request, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error while creating product"));
        }
    }


    /**
     * Delete request to delete product
     *
     * @param orderId - order id
     * @return Operation {@link HttpStatus} status
     */
    @DeleteMapping(value = "/{order_id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("order_id") @Positive(message = "orderId must be positive") int orderId) {
        try {
            log.debug("Deleting order");
            this.orderService.deleteOrderById(orderId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (EntityNotFoundException ex) {
            log.warn("Entity not found, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
        } catch (Exception ex) {
            log.warn("General error while processing request, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error while deleting product"));
        }
    }
}
