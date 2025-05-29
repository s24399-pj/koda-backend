package pl.pjwstk.kodabackend.offer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.pjwstk.kodabackend.offer.persistance.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/offers/search")
@RequiredArgsConstructor
public class NativeOfferController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/native")
    public ResponseEntity<List<Map<String, Object>>> searchOffersNative(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer priceFrom,
            @RequestParam(required = false) Integer priceTo,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            @RequestParam(required = false) Integer mileageFrom,
            @RequestParam(required = false) Integer mileageTo,
            @RequestParam(required = false) Integer enginePowerFrom,
            @RequestParam(required = false) Integer enginePowerTo,
            @RequestParam(required = false) Integer doors,
            @RequestParam(required = false) Integer seats,
            @RequestParam(required = false) Boolean firstOwner,
            @RequestParam(required = false) Boolean accidentFree,
            @RequestParam(required = false) Boolean serviceHistory,
            // ENUMY jako listy
            @RequestParam(required = false) List<String> fuelType,
            @RequestParam(required = false) List<String> transmission,
            @RequestParam(required = false) List<String> bodyType,
            @RequestParam(required = false) List<String> driveType,
            @RequestParam(required = false) List<String> condition,
            @RequestParam(defaultValue = "20") int size) {

        log.info("🔍 Native search - brand: {}, model: {}, searchTerm: {}, fuelType: {}, transmission: {}",
                brand, model, searchTerm, fuelType, transmission);

        StringBuilder sql = new StringBuilder("""
            SELECT 
                o.id,
                o.title, 
                o.price,
                (SELECT oi.url FROM offer_images oi WHERE oi.offer_id = o.id AND oi.is_primary = true) as main_image,
                cd.mileage,
                cd.fuel_type,
                cd.year,
                cd.engine_power,
                cd.engine_displacement,
                cd.brand,
                cd.model,
                cd.color,
                cd.transmission,
                cd.body_type,
                cd.drive_type,
                cd.condition,
                cd.doors,
                cd.seats,
                cd.first_owner,
                cd.accident_free,
                cd.service_history
            FROM offers o 
            JOIN car_details cd ON o.car_details_id = cd.id 
            WHERE 1=1
            """);

        List<Object> params = new ArrayList<>();

        // Podstawowe filtry
        if (brand != null && !brand.trim().isEmpty()) {
            sql.append(" AND LOWER(cd.brand) = LOWER(?)");
            params.add(brand);
        }

        if (model != null && !model.trim().isEmpty()) {
            sql.append(" AND LOWER(cd.model) LIKE LOWER(?)");
            params.add("%" + model + "%");
        }

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql.append(" AND (LOWER(o.title) LIKE LOWER(?) OR LOWER(o.description) LIKE LOWER(?))");
            params.add("%" + searchTerm + "%");
            params.add("%" + searchTerm + "%");
        }

        if (location != null && !location.trim().isEmpty()) {
            sql.append(" AND LOWER(o.location) LIKE LOWER(?)");
            params.add("%" + location + "%");
        }

        // Filtry liczbowe
        if (priceFrom != null) {
            sql.append(" AND o.price >= ?");
            params.add(priceFrom);
        }

        if (priceTo != null) {
            sql.append(" AND o.price <= ?");
            params.add(priceTo);
        }

        if (yearFrom != null) {
            sql.append(" AND cd.year >= ?");
            params.add(yearFrom);
        }

        if (yearTo != null) {
            sql.append(" AND cd.year <= ?");
            params.add(yearTo);
        }

        if (mileageFrom != null) {
            sql.append(" AND cd.mileage >= ?");
            params.add(mileageFrom);
        }

        if (mileageTo != null) {
            sql.append(" AND cd.mileage <= ?");
            params.add(mileageTo);
        }

        if (enginePowerFrom != null) {
            sql.append(" AND cd.engine_power >= ?");
            params.add(enginePowerFrom);
        }

        if (enginePowerTo != null) {
            sql.append(" AND cd.engine_power <= ?");
            params.add(enginePowerTo);
        }

        if (doors != null) {
            sql.append(" AND cd.doors = ?");
            params.add(doors);
        }

        if (seats != null) {
            sql.append(" AND cd.seats = ?");
            params.add(seats);
        }

        // Boolean filtry
        if (firstOwner != null) {
            sql.append(" AND cd.first_owner = ?");
            params.add(firstOwner);
        }

        if (accidentFree != null) {
            sql.append(" AND cd.accident_free = ?");
            params.add(accidentFree);
        }

        if (serviceHistory != null) {
            sql.append(" AND cd.service_history = ?");
            params.add(serviceHistory);
        }

        // ENUMY - tutaj jest klucz!
        if (fuelType != null && !fuelType.isEmpty()) {
            sql.append(" AND cd.fuel_type IN (");
            for (int i = 0; i < fuelType.size(); i++) {
                sql.append(i > 0 ? ",?" : "?");
                params.add(fuelType.get(i));
            }
            sql.append(")");
        }

        if (transmission != null && !transmission.isEmpty()) {
            sql.append(" AND cd.transmission IN (");
            for (int i = 0; i < transmission.size(); i++) {
                sql.append(i > 0 ? ",?" : "?");
                params.add(transmission.get(i));
            }
            sql.append(")");
        }

        if (bodyType != null && !bodyType.isEmpty()) {
            sql.append(" AND cd.body_type IN (");
            for (int i = 0; i < bodyType.size(); i++) {
                sql.append(i > 0 ? ",?" : "?");
                params.add(bodyType.get(i));
            }
            sql.append(")");
        }

        if (driveType != null && !driveType.isEmpty()) {
            sql.append(" AND cd.drive_type IN (");
            for (int i = 0; i < driveType.size(); i++) {
                sql.append(i > 0 ? ",?" : "?");
                params.add(driveType.get(i));
            }
            sql.append(")");
        }

        if (condition != null && !condition.isEmpty()) {
            sql.append(" AND cd.condition IN (");
            for (int i = 0; i < condition.size(); i++) {
                sql.append(i > 0 ? ",?" : "?");
                params.add(condition.get(i));
            }
            sql.append(")");
        }

        sql.append(" ORDER BY o.created_at DESC LIMIT ?");
        params.add(size);

        log.info("🔍 Executing SQL: {}", sql.toString());
        log.info("📋 With params: {}", params);

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql.toString(), params.toArray());

        log.info("✅ Found {} offers", results.size());

        return ResponseEntity.ok(results);
    }

    // Pozostałe endpointy bez zmian...
    @GetMapping("/all-native")
    public ResponseEntity<List<Map<String, Object>>> getAllOffersNative() {
        String sql = """
            SELECT 
                o.id,
                o.title, 
                o.price,
                cd.mileage,
                cd.fuel_type,
                cd.year,
                cd.engine_power,
                cd.engine_displacement,
                cd.brand,
                cd.model
            FROM offers o 
            JOIN car_details cd ON o.car_details_id = cd.id 
            ORDER BY o.created_at DESC 
            LIMIT 20
            """;

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/brands")
    public ResponseEntity<List<String>> getBrands() {
        String sql = "SELECT DISTINCT brand FROM car_details ORDER BY brand";
        List<String> brands = jdbcTemplate.queryForList(sql, String.class);
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/test-connection")
    public ResponseEntity<Map<String, Object>> testConnection() {
        String sql = "SELECT COUNT(*) as offer_count FROM offers";
        Map<String, Object> result = jdbcTemplate.queryForMap(sql);
        return ResponseEntity.ok(result);
    }
}