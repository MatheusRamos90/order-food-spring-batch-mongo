package br.com.matheushramos.batch.model;

import br.com.matheushramos.batch.dto.FoodData;
import br.com.matheushramos.batch.enums.StatusEnum;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "order")
@Data
@Builder
public class Order {

    @Id
    private ObjectId id;

    private UUID orderId;
    private List<FoodData> foodData;
    private StatusEnum status;

}
