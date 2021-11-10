package ru.marketboost.ransom.selenium.imitation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import ru.marketboost.ransom.utils.Randomizer;

@Data
@Builder
@AllArgsConstructor
public class Moving {

    private final Rectangle from;
    private final Rectangle to;

    public void start() {
        Point start;
        Point finish = new Point(Randomizer.randomBetween(to.x, to.x + to.width), Randomizer.randomBetween(to.y, to.y + to.height));
    }

    private void move(Readable from, Readable to) {

    }


}
