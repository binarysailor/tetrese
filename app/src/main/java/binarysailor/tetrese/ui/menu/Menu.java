package binarysailor.tetrese.ui.menu;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class Menu {

    private final Collection<MenuItem> items;

    Menu(Collection<MenuItem> items) {
        this.items = Collections.unmodifiableCollection(items);
    }

    public Collection<MenuItem> getItems() {
        return items;
    }

    public Optional<MenuItem> getItemAt(int x, int y) {
        Point p = new Point(x, y);
        return items.stream().filter(i -> i.containsPoint(p)).findFirst();
    }

    static MenuBuilder inPanel(Rect panelBounds) {
        return new MenuBuilder(panelBounds);
    }

    static class MenuBuilder {
        private Rect panelBounds;
        private Paint textPaint;
        private Collection<Option> options = new ArrayList<>();

        public MenuBuilder(Rect panelBounds) {
            this.panelBounds = panelBounds;
        }

        MenuBuilder withTextPaint(Paint paint) {
            this.textPaint = paint;
            return this;
        }

        MenuBuilder addOption(Object tag, String text) {
            options.add(new Option(text, tag));
            return this;
        }

        Menu build() {
            return new Menu(buildMenuItems());
        }

        private Collection<MenuItem> buildMenuItems() {
            int optionCount = options.size();
            MenuMeasurements m = MenuMeasurements.calculate(options, textPaint);

            int allItemsAndSpacingHeight = optionCount * m.itemHeight + (optionCount - 1) * m.itemSpacing;

            int firstItemTop = offsetToCenter(allItemsAndSpacingHeight, panelBounds.height());
            int itemLeft = offsetToCenter(m.itemWidth, panelBounds.width());

            Collection<MenuItem> result = new ArrayList<>(optionCount);
            int counter = 0;
            for (Option option : options) {
                int itemTop = firstItemTop + counter * (m.itemHeight + m.itemSpacing);
                RectF itemBounds = new RectF(
                        itemLeft, itemTop,itemLeft + m.itemWidth,itemTop + m.itemHeight);

                Rect textSize = m.getOptionTextSize(option);
                int textLeft = itemLeft + offsetToCenter(textSize.width(), m.itemWidth);
                int textBottom = itemTop + offsetToCenter(textSize.height(), m.itemHeight) + textSize.height();

                result.add(new MenuItem(
                        option.tag, option.text,
                        itemBounds,
                        new Point(textLeft, textBottom)));

                counter++;
            }

            return result;
        }

        private int offsetToCenter(int inner, int outer) {
            return (outer - inner) / 2;
        }

        private static class Option {
            private String text;
            private Object tag;

            public Option(String text, Object tag) {
                this.text = text;
                this.tag = tag;
            }
        }

        private static class MenuMeasurements {
            private final Map<Option, Rect> textSizeMap;
            final int itemHeight;
            final int itemWidth;
            final int itemSpacing;

            private MenuMeasurements(Map<Option, Rect> boundsMap, int itemWidth, int itemHeight, int itemSpacing) {
                this.textSizeMap = boundsMap;
                this.itemWidth = itemWidth;
                this.itemHeight = itemHeight;
                this.itemSpacing = itemSpacing;
            }

            Rect getOptionTextSize(Option option) {
                return textSizeMap.get(option);
            }

            static MenuMeasurements calculate(Iterable<Option> options, Paint textPaint) {
                Map<Option, Rect> boundsMap = new HashMap<>();
                int maxWidth = 0;
                int maxHeight = 0;
                for (Option option : options) {
                    Rect bounds = new Rect();
                    textPaint.getTextBounds(option.text, 0, option.text.length(), bounds);
                    boundsMap.put(option, bounds);
                    maxWidth = Math.max(maxWidth, bounds.width());
                    maxHeight = Math.max(maxHeight, bounds.height());
                }

                int itemHeight = (int)(maxHeight * 1.8f); // preferred
                int itemSpacing = (int)(itemHeight * 0.75f);
                int itemWidth = (int)(maxWidth * 1.3f);

                return new MenuMeasurements(boundsMap, itemWidth, itemHeight, itemSpacing);
            }
        }
    }
}
