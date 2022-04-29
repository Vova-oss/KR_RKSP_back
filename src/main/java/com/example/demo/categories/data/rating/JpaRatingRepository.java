package com.example.demo.categories.data.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaRatingRepository extends JpaRepository<RatingDataMapper, Long> {
}
