package rentsphere.catalogservice.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface HouseRepository extends CrudRepository<House, Long> {
    Optional<House> findByCode(String code);
    boolean existsByCode(String code);

    @Modifying
    @Transactional
    @Query("delete from House where code = :code")
    void deleteByCode(String code);
}
