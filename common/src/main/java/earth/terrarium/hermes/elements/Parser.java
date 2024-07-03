package earth.terrarium.hermes.elements;

import dev.dediamondpro.minemark.MineMarkCore;
import dev.dediamondpro.minemark.MineMarkCoreBuilder;
import dev.dediamondpro.minemark.elements.Elements;
import dev.dediamondpro.minemark.elements.MineMarkElement;
import dev.dediamondpro.minemark.elements.formatting.impl.AlignmentElement;
import dev.dediamondpro.minemark.elements.formatting.impl.CssStyleElement;
import dev.dediamondpro.minemark.elements.impl.LinkElement;
import dev.dediamondpro.minemark.elements.impl.ParagraphElement;
import dev.dediamondpro.minemark.elements.impl.table.TableRowElement;
import earth.terrarium.hermes.elements.base.NoOpElement;
import earth.terrarium.hermes.elements.custom.HermesItem;
import earth.terrarium.hermes.elements.custom.HermesText;
import earth.terrarium.hermes.elements.html.*;
import earth.terrarium.hermes.elements.html.details.HtmlDetails;
import earth.terrarium.hermes.elements.html.details.HtmlSummary;
import earth.terrarium.hermes.elements.html.list.HtmlList;
import earth.terrarium.hermes.elements.html.list.HtmlListItem;
import earth.terrarium.hermes.elements.html.map.HtmlArea;
import earth.terrarium.hermes.elements.html.map.HtmlMap;
import earth.terrarium.hermes.elements.html.table.HtmlCaption;
import earth.terrarium.hermes.elements.html.table.HtmlTable;
import earth.terrarium.hermes.elements.html.table.HtmlTableCell;
import earth.terrarium.hermes.renderer.HermesRenderer;
import earth.terrarium.hermes.styles.HermesStyle;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class Parser {

    private static final Pattern PREFIX_PATTERN = Pattern.compile("^ +<", Pattern.MULTILINE);
    private static final Pattern SUFFIX_PATTERN = Pattern.compile("> +$", Pattern.MULTILINE);

    private final MineMarkCore<HermesStyle, HermesRenderer> core;
    private final HermesStyle style;

    public Parser(HermesStyle style) {
        this(style, builder -> {
            builder.addExtension(StrikethroughExtension.builder().requireTwoTildes(true).build());
            builder.addExtension(TablesExtension.create());

            builder.setTextElement(HtmlParagraph::new);

            builder.addElement(Elements.HEADING, HtmlHeading::new);
            builder.addElement(List.of("p", "text"), ParagraphElement::new);
            builder.addElement(Elements.LINK, LinkElement::new);

            builder.addElement(List.of("ol", "ul", "dl", "menu"), HtmlList::new);
            builder.addElement(List.of("li", "dt", "dd"), HtmlListItem::new);

            builder.addElement(Elements.TABLE, HtmlTable::new);
            builder.addElement(Elements.TABLE_ROW, TableRowElement::new);
            builder.addElement(Elements.TABLE_CELL, HtmlTableCell::new);
            builder.addElement("caption", HtmlCaption::new);

            builder.addElement(Elements.IMAGE, HtmlImage::new);
            builder.addElement("embed", HtmlImage::forEmbed);
            builder.addElement("object", HtmlImage::forObject);
            builder.addElement(Elements.HORIZONTAL_RULE, HtmlHorizontalRule::new);

            builder.addElement("details", HtmlDetails::new);
            builder.addElement("summary", HtmlSummary::new);

            builder.addElement("figure", HtmlFigure::new);

            builder.addElement(Elements.CODE_BLOCK, HtmlCodeBlock::new);
            builder.addElement(Elements.BLOCKQUOTE, HtmlBlockQuote::new);

            builder.addElement("map", HtmlMap::new);
            builder.addElement("area", HtmlArea::new);

            builder.addElement("progress", HtmlProgress::new);
            builder.addElement("meter", HtmlMeter::new);

            builder.addElement("item", HermesItem::new);


            builder.addFormatingElement(new HermesText());
            builder.addFormatingElement(new HtmlFont());
            builder.addFormatingElement(new AttributesGlobal());
            builder.addFormatingElement(new AlignmentElement<>());
            builder.addFormatingElement(new CssStyleElement<>());

            builder.addElement(NoOpElement.CREATOR);
            builder.addElement(HtmlDefault.CREATOR);
        });
    }

    public Parser(HermesStyle style, Consumer<MineMarkCoreBuilder<HermesStyle, HermesRenderer>> factory) {
        var builder = MineMarkCore.<HermesStyle, HermesRenderer>builder()
                .withoutDefaultElements()
                .withoutDefaultFormattingElements();
        factory.accept(builder);
        this.core = builder.build();
        this.style = style;
    }

    @Nullable
    public MineMarkElement<HermesStyle, HermesRenderer> parse(String text) {
        try {
            text = PREFIX_PATTERN.matcher(text).replaceAll("<");
            text = SUFFIX_PATTERN.matcher(text).replaceAll(">");
            this.style.globalData().clear();
            return core.parse(this.style, text);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
