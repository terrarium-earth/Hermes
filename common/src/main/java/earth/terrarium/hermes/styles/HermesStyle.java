package earth.terrarium.hermes.styles;

import dev.dediamondpro.minemark.style.*;
import earth.terrarium.hermes.data.GlobalData;

public class HermesStyle implements Style {

    private final TextStyleConfig textStyle;
    private final ParagraphStyleConfig paragraphStyle;
    private final LinkStyleConfig linkStyle;
    private final HeadingStyleConfig headingStyle;
    private final HorizontalRuleStyleConfig horizontalRuleStyle;
    private final ImageStyleConfig imageStyle;
    private final ListStyleConfig listStyle;
    private final HermesBlockquoteStyle blockquoteStyle;
    private final CodeBlockStyleConfig codeBlockStyle;
    private final TableStyleConfig tableStyle;

    private final GlobalData globalData = new GlobalData();

    public HermesStyle(TextStyleConfig textStyle, ParagraphStyleConfig paragraphStyle, LinkStyleConfig linkStyle, HeadingStyleConfig headingStyle, HorizontalRuleStyleConfig horizontalRuleStyle, ImageStyleConfig imageStyle, ListStyleConfig listStyle, HermesBlockquoteStyle blockquoteStyle, CodeBlockStyleConfig codeBlockStyle, TableStyleConfig tableStyle) {
        this.textStyle = textStyle;
        this.paragraphStyle = paragraphStyle;
        this.linkStyle = linkStyle;
        this.headingStyle = headingStyle;
        this.horizontalRuleStyle = horizontalRuleStyle;
        this.imageStyle = imageStyle;
        this.listStyle = listStyle;
        this.blockquoteStyle = blockquoteStyle;
        this.codeBlockStyle = codeBlockStyle;
        this.tableStyle = tableStyle;
    }

    public HermesStyle() {
        this(
                DefaultStyle.TEXT_STYLE,
                DefaultStyle.PARAGRAPH_STYLE,
                DefaultStyle.LINK_STYLE,
                DefaultStyle.HEADING_STYLE,
                DefaultStyle.HR_STYLE,
                DefaultStyle.IMAGE_STYLE,
                DefaultStyle.LIST_STYLE,
                DefaultStyle.BLOCKQUOTE_STYLE,
                DefaultStyle.CODEBLOCK_STYLE,
                DefaultStyle.TABLE_STYLE
        );
    }

    public GlobalData globalData() {
        return globalData;
    }

    @Override
    public TextStyleConfig getTextStyle() {
        return textStyle;
    }

    @Override
    public ParagraphStyleConfig getParagraphStyle() {
        return paragraphStyle;
    }

    @Override
    public LinkStyleConfig getLinkStyle() {
        return linkStyle;
    }

    @Override
    public HeadingStyleConfig getHeadingStyle() {
        return headingStyle;
    }

    @Override
    public HorizontalRuleStyleConfig getHorizontalRuleStyle() {
        return horizontalRuleStyle;
    }

    @Override
    public ImageStyleConfig getImageStyle() {
        return imageStyle;
    }

    @Override
    public ListStyleConfig getListStyle() {
        return listStyle;
    }

    @Override
    public HermesBlockquoteStyle getBlockquoteStyle() {
        return blockquoteStyle;
    }

    @Override
    public CodeBlockStyleConfig getCodeBlockStyle() {
        return codeBlockStyle;
    }

    @Override
    public TableStyleConfig getTableStyle() {
        return tableStyle;
    }
}