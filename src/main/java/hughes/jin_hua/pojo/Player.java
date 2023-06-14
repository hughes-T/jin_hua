package hughes.jin_hua.pojo;

import lombok.Data;

import java.util.List;

@Data
public class Player {

    private String name;

    private String password;

    private List<Card> cards;

    /**
     * 余额
     */
}
