package com.backend.doyouhave.domain.resign;

import com.backend.doyouhave.domain.BaseTimeEntity;
import com.backend.doyouhave.domain.user.dto.LoginResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ResignReason extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resign_reason_id")
    private Long id;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private int count = 0;

    @Builder
    public ResignReason(String reason) {
        this.reason = reason;
    }

    public static ResignReason from(String reason) {
        return ResignReason.builder()
                .reason(reason)
                .build();
    }
}
