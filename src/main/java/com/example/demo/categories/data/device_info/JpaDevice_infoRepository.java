package com.example.demo.categories.data.device_info;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaDevice_infoRepository extends JpaRepository<Device_infoDataMapper, Long> {
}
