package earth.terrarium.hermes.styles;

import dev.dediamondpro.minemark.style.*;
import earth.terrarium.hermes.image.HermesImageProvider;
import net.minecraft.Util;

import java.awt.*;

public final class DefaultStyle {

    private static final Color LINE_COLOR = new Color(80, 80, 80);

    // Defaults
    public static final TextStyleConfig TEXT_STYLE = new TextStyleConfig(1f, Color.WHITE, 2f, size -> size / 16f);
    public static final ParagraphStyleConfig PARAGRAPH_STYLE = new ParagraphStyleConfig(6f);
    public static final LinkStyleConfig LINK_STYLE = new LinkStyleConfig(new Color(65, 105, 225), Util.getPlatform()::openUri);
    public static final HeadingStyleConfig HEADING_STYLE = new HeadingStyleConfig(
            new HeadingLevelStyleConfig(2f, 12f, true, LINE_COLOR, 2f, 5f),
            new HeadingLevelStyleConfig(1.66f, 10f, true, LINE_COLOR, 2f, 5f),
            new HeadingLevelStyleConfig(1.33f, 8f),
            new HeadingLevelStyleConfig(1f, 6f),
            new HeadingLevelStyleConfig(0.7f, 4f),
            new HeadingLevelStyleConfig(0.7f, 4f)
    );
    public static final HorizontalRuleStyleConfig HR_STYLE = new HorizontalRuleStyleConfig(2f, 4f, LINE_COLOR);
    public static final ImageStyleConfig IMAGE_STYLE = new ImageStyleConfig(HermesImageProvider.INSTANCE);
    public static final ListStyleConfig LIST_STYLE = new ListStyleConfig(16f, 6f);
    public static final HermesBlockquoteStyle BLOCKQUOTE_STYLE = new HermesBlockquoteStyle(
            6f, 4f, 2f, 10f, LINE_COLOR, new Color(0, 0, 0, 50)
    );
    public static final CodeBlockStyleConfig CODEBLOCK_STYLE = new CodeBlockStyleConfig(2f, 1f, 6f, 6f, LINE_COLOR);
    public static final TableStyleConfig TABLE_STYLE = new TableStyleConfig(
            6f, 4f, 1f, LINE_COLOR,
            new Color(0, 0, 0, 150),
            new Color(0, 0, 0, 50)
    );

}
