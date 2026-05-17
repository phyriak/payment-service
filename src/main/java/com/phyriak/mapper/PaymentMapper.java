package com.phyriak.mapper;

import com.phyriak.dto.PaymentDto;
import com.phyriak.dto.PaymentRequest;
import com.phyriak.repository.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    Payment paymentRequestToEntity(PaymentRequest paymentRequest);

    @Mapping(target = "paymentStatus", source = "paymentStatus")
    @Mapping(target = "amount", source = "amount")
    PaymentDto paymentEntityToPaymentDto(Payment payment);
}
