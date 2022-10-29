package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Request {

    private Integer id;

    private String date;
    private String client;
    private Boolean delivered;
    private Product product;



    @Override
    public String toString() {
        return "Request{" + "id=" + id + ", date=" + date + ", client=" + client + ", status=" + delivered + ", product=" + product + '}';
    }
}
