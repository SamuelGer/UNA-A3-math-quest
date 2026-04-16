package com.mathquest.mathquest_backend.repository;

import com.mathquest.mathquest_backend.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
}
