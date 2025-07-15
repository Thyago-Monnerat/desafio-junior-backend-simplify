package org.thyagomonnerat.desafiojunior.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thyagomonnerat.desafiojunior.models.TaskModel;

public interface TaskRepository extends JpaRepository<TaskModel, Long> {
}
