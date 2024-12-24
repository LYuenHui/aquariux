package com.aquariux.trade.app.repository;

import com.aquariux.trade.app.entity.CryptoPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CryptoPairRepository extends JpaRepository<CryptoPair, Long> {
    Optional<CryptoPair> findByPairName(String pairName);
}
