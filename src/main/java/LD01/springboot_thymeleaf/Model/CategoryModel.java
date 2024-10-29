package LD01.springboot_thymeleaf.Model;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private long categoryId;

    @NotEmpty(message = "Không được bỏ trống!")
    private String name;

    private Boolean isEdit = false;
}
