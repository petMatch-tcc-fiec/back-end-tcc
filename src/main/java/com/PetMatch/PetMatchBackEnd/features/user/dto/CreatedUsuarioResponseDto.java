package com.PetMatch.PetMatchBackEnd.features.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatedUsuarioResponseDto {
    String id;
    String userId;
}
