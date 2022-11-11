package cn.wolfcode.entity;

import lombok.*;

/**
 * @author wby
 * @version 1.0
 * @date 2022/11/11 9:49
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Leave {
    private Long id;
    private String userName;
    private Integer days;
    private String reason;
}
