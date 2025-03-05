package pl.pjwstk.kodabackend.offer.persistance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "offer_images")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferImage {

    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String url;
    private String caption;

    @Builder.Default
    private boolean primary = false;

    private int sortOrder;
}
