package com.nova.fieldops.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    Optional<Device> findByDeviceCode(String deviceCode);

    @Query("""
            SELECT d
            FROM Device d
            JOIN FETCH d.site
            WHERE d.id = :id
            """)
    Optional<Device> findByIdWithSite(@Param("id") Long id);
}