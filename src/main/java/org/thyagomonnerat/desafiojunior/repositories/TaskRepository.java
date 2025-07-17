package org.thyagomonnerat.desafiojunior.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thyagomonnerat.desafiojunior.models.TaskModel;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskModel, Long> {
    Optional<TaskModel> findByName(String name);
}
