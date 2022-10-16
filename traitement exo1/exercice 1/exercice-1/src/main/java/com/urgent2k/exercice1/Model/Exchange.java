package com.urgent2k.exercice1.Model;

import lombok.Data;

@Data
public class Exchange {
    private String result;
    private String documentation;
    private String terms_of_use;
    private Integer time_last_update_unix;
    private  String time_last_update_utc;
    private Integer time_next_update_unix;
    private String time_next_update_utc;
    private String base_code;
    private String target_code;
    private float conversion_rate;
    private float conversion_result;
}
