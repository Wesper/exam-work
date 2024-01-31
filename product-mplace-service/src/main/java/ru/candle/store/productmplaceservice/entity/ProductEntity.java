package ru.candle.store.productmplaceservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class ProductEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_id")
    @NonNull
    private String imageId;

    @Column(name = "title")
    @NonNull
    private String title;

    @Column(name = "description")
    @NonNull
    private String description;

    @Column(name = "subtitle")
    @NonNull
    private String subtitle;

    @Column(name = "price")
    @NonNull
    private Long price;

    @Column(name = "type")
    @NonNull
    private String type;

    @Column(name = "measure")
    @NonNull
    private String measure;

    @Column(name = "unit_measure")
    @NonNull
    private String unitMeasure;

    @Column(name = "actual")
    @NonNull
    private Boolean actual;

    public ProductEntity(@NonNull Long id, @NonNull String imageId, @NonNull String title, @NonNull String description, @NonNull String subtitle, @NonNull Long price, @NonNull String type, @NonNull String measure, @NonNull String unitMeasure, @NonNull Boolean actual) {
        this.id = id;
        this.imageId = imageId;
        this.title = title;
        this.description = description;
        this.subtitle = subtitle;
        this.price = price;
        this.type = type;
        this.measure = measure;
        this.unitMeasure = unitMeasure;
        this.actual = actual;
    }
}
