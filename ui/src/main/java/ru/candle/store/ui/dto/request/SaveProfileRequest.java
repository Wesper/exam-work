package ru.candle.store.ui.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveProfileRequest {

    @Size(max = 32, message = "Поле должно иметь размер до 32")
    @NonNull
    private String firstName;

    @Size(max = 32, message = "Поле должно иметь размер до 32")
    @NonNull
    private String lastName;

    @Size(max = 32, message = "Поле должно иметь размер до 32")
    private String middleName;

    @Size(max = 32, message = "Поле должно иметь размер до 32")
    private String city;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NonNull
    private String birthday;

    @Size(max = 32, message = "Поле должно иметь размер до 32")
    private String address;

}
