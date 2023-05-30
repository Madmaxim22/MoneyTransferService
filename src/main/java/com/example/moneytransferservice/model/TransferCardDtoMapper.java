package com.example.moneytransferservice.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransferCardDtoMapper {
    TransferCardDto INSTANCE = Mappers.getMapper(TransferCardDto.class);

    TransferCardDto transferCardToTransferCardDto(TransferCard transferCard);

    TransferCard transferCardDtoToTransferCard(TransferCardDto transferCardDto);
}
