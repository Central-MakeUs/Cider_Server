package com.cmc.memberLevel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_level")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLevel {

    @Id
    @Column(name = "member_level_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer memberLevelId;

    private String levelName;

    private Integer requiredExperience;

    public static String getPercentComment(int percent) {

        switch (percent / 20) {
            case 0:
                return "좋은 시작이에요!";
            case 1:
                return "꾸준히 발전하고 있어요. 계속 나아가세요!";
            case 2:
                return "잘하고 있어요. 힘내세요!";
            case 3:
                return "거의 다 왔어요. 조금만 더 화이팅!";
            case 4:
                return "마지막 구간이예요. 한 걸음만 더 화이팅!";
            default:
                return "퍼센트 계산 오류.....";
        }
    }
}
