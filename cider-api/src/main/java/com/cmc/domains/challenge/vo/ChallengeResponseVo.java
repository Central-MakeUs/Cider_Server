package com.cmc.domains.challenge.vo;

import com.cmc.challenge.Challenge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeResponseVo {

    private Challenge challenge;

    private Integer participateNum;

}
