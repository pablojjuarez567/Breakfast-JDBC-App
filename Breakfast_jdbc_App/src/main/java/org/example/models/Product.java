package org.example.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Integer id;

    private String name;
    private String type;
    private Float price;
    private Boolean availibity;



    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name=" + name + ", type=" + type + ", price=" + price + ", availibity=" + availibity + '}';
    }
}
