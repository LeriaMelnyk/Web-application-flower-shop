package floristry.shop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "flowers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Flower {

    @Id
    private String id;
    private String name;
    private String category;
    private double price;
    private int stock;
    private String description;
}