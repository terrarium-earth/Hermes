package earth.terrarium.hermes.impl;

import dev.dediamondpro.minemark.style.*;
import earth.terrarium.hermes.api.rendering.HtmlBlockquoteStyleConfig;
import earth.terrarium.hermes.api.rendering.HtmlStyle;

public final class HermesStyle implements HtmlStyle {

    private final TextStyleConfig textStyle;
    private final ParagraphStyleConfig paragraphStyle;
    private final LinkStyleConfig linkStyle;
    private final HeadingStyleConfig headingStyle;
    private final HorizontalRuleStyleConfig horizontalRuleStyle;
    private final ImageStyleConfig imageStyle;
    private final ListStyleConfig listStyle;
    private final HtmlBlockquoteStyleConfig blockquoteStyle;
    private final CodeBlockStyleConfig codeBlockStyle;
    private final TableStyleConfig tableStyle;

    private final GlobalData globalData = new GlobalData();

    public HermesStyle(TextStyleConfig textStyle, ParagraphStyleConfig paragraphStyle, LinkStyleConfig linkStyle, HeadingStyleConfig headingStyle, HorizontalRuleStyleConfig horizontalRuleStyle, ImageStyleConfig imageStyle, ListStyleConfig listStyle, HtmlBlockquoteStyleConfig blockquoteStyle, CodeBlockStyleConfig codeBlockStyle, TableStyleConfig tableStyle) {
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

    @Override
    public void onStart() {
        globalData.clear();
    }

    @Override
    public GlobalData data() {
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
    public HtmlBlockquoteStyleConfig getBlockquoteStyle() {
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