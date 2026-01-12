package oxy.bascenario.utils.font;

import java.util.*;

import imgui.*;
import lombok.SneakyThrows;
import net.raphimc.thingl.ThinGL;
import net.raphimc.thingl.gl.renderer.impl.RendererText;
import net.raphimc.thingl.resource.font.Font;
import net.raphimc.thingl.resource.font.impl.FreeTypeFont;
import net.raphimc.thingl.text.TextRun;
import net.raphimc.thingl.text.shaping.ShapedTextLine;
import net.raphimc.thingl.text.shaping.ShapedTextRun;
import oxy.bascenario.Base;
import oxy.bascenario.api.Scenario;
import oxy.bascenario.api.render.elements.text.font.FontStyle;
import oxy.bascenario.api.render.elements.text.TextSegment;
import oxy.bascenario.api.render.elements.text.font.FontType;
import oxy.bascenario.api.utils.FileInfo;

import static oxy.bascenario.utils.ThinGLUtils.GLOBAL_RENDER_STACK;

public class FontUtils {
    public static Font DEFAULT, SEMI_BOLD;
    private static final Map<String, Font> NAME_TO_FONTS = new HashMap<>();

    public static ImFont IM_FONT_SEMI_BOLD_20, IM_FONT_SEMI_BOLD_30, IM_FONT_REGULAR_35, CHILLGOTHIC_17, IM_CONTROL_DEFAULT;

    public static Font font(FontStyle style, FontType type) {
        return NAME_TO_FONTS.get(type.toName(style));
    }

    public static Font toFont(Scenario scenario, TextSegment segment) {
        Font font;
        if (segment.font().file() != null) {
            font = NAME_TO_FONTS.get(String.valueOf(segment.font().file().hashCode(scenario.getName())));
            if (font == null) {
                font = FontUtils.loadSpecificFont(scenario, segment.font().file());
                NAME_TO_FONTS.put(String.valueOf(segment.font().file().hashCode(scenario.getName())), font);
            }
        } else {
            font = NAME_TO_FONTS.get(segment.font().type().toName(segment.font().style()));
        }
        return font;
    }

    public static void loadFonts() {
        // Cache these font, so I can use them dynamically later.
        // Walker's TODO: Optimize these load codes.

        // Global
        loadFont("NotoSansRegular", "/assets/base/fonts/global/NotoSans-Regular.ttf");
        loadFont("NotoSansSemiBold", "/assets/base/fonts/global/NotoSans-SemiBold.ttf");
        loadFont("NotoSansBold", "/assets/base/fonts/global/NotoSans-Bold.ttf");

        // Korea
        loadFont("GyeonggiRegular", "/assets/base/fonts/korea/Gyeonggi_Medium.ttf");
        loadFont("GyeonggiSemiBold", "/assets/base/fonts/korea/Gyeonggi_Medium.ttf");
        loadFont("GyeonggiBold", "/assets/base/fonts/korea/Gyeonggi_Bold.ttf");

        // Japan
        loadFont("ShinMaruGoRegular", "/assets/base/fonts/japan/U-OTF-ShinMGoUpr-Medium.otf");
        loadFont("ShinMaruGoSemiBold", "/assets/base/fonts/japan/A-OTF Shin Maru Go Pro DB.otf");
        loadFont("ShinMaruGoBold", "/assets/base/fonts/japan/A-OTF Shin Maru Go Pro DB.otf");

        // Simplified Chinese
        loadFont("ChillRoundRegular", "/assets/base/fonts/chinese/simplified/ChillRoundGothic_Regular.otf");
        loadFont("ChillRoundSemiBold", "/assets/base/fonts/chinese/simplified/ChillRoundGothic_Medium.otf");
        loadFont("ChillRoundBold", "/assets/base/fonts/chinese/simplified/ChillRoundGothic_Bold.otf");

        // Traditional Chinese
        loadFont("NotoSansTCRegular", "/assets/base/fonts/chinese/traditional/NotoSansTC-Regular.ttf");
        loadFont("NotoSansTCSemiBold", "/assets/base/fonts/chinese/traditional/NotoSansTC-SemiBold.ttf");
        loadFont("NotoSansTCBold", "/assets/base/fonts/chinese/traditional/NotoSansTC-Bold.ttf");

        Locale locale = Locale.getDefault(); // Walker: I can't find out a better way to load these Fonts.
        System.out.printf("%s\n", locale.getCountry());
        switch (locale.getCountry()) {
            case "KR":
                ImGui.getIO().setFontDefault(loadImFont("/assets/base/fonts/korea/Gyeonggi_Regular.ttf", 17, true));
                DEFAULT = NAME_TO_FONTS.get("GyeonggiRegular");
                SEMI_BOLD = NAME_TO_FONTS.get("GyeonggiSemiBold");
                IM_FONT_SEMI_BOLD_20 = loadImFont("/assets/base/fonts/korea/Gyeonggi_Medium.ttf", 20, true);
                IM_FONT_SEMI_BOLD_30 = loadImFont("/assets/base/fonts/korea/Gyeonggi_Medium.ttf", 30, true);
                IM_FONT_REGULAR_35 = loadImFont("/assets/base/fonts/korea/Gyeonggi_Regular.ttf", 35, true);
                IM_CONTROL_DEFAULT = loadImFont("/assets/base/fonts/korea/Gyeonggi_Regular.ttf", 17, true);
                break;
            case "JP":
                ImGui.getIO().setFontDefault(loadImFont("/assets/base/fonts/japan/U-OTF-ShinMGoUpr-Medium.otf", 17, true));
                DEFAULT = NAME_TO_FONTS.get("ShinMaruGoRegular");
                SEMI_BOLD = NAME_TO_FONTS.get("ShinMaruGoSemiBold");
                IM_FONT_SEMI_BOLD_20 = loadImFont("/assets/base/fonts/japan/A-OTF Shin Maru Go Pro DB.otf", 20, true);
                IM_FONT_SEMI_BOLD_30 = loadImFont("/assets/base/fonts/japan/A-OTF Shin Maru Go Pro DB.otf", 30, true);
                IM_FONT_REGULAR_35 = loadImFont("/assets/base/fonts/japan/U-OTF-ShinMGoUpr-Medium.otf", 35, true);
                IM_CONTROL_DEFAULT = loadImFont("/assets/base/fonts/japan/U-OTF-ShinMGoUpr-Medium.otf", 17, true);
                break;
            case "CN":
                ImGui.getIO().setFontDefault(loadImFont("/assets/base/fonts/chinese/simplified/ChillRoundGothic_Regular.otf", 17, true));
                DEFAULT = NAME_TO_FONTS.get("ChillRoundRegular");
                SEMI_BOLD = NAME_TO_FONTS.get("ChillRoundSemiBold");
                IM_FONT_SEMI_BOLD_20 = loadImFont("/assets/base/fonts/chinese/simplified/ChillRoundGothic_Medium.otf", 20, true);
                IM_FONT_SEMI_BOLD_30 = loadImFont("/assets/base/fonts/chinese/simplified/ChillRoundGothic_Medium.otf", 30, true);
                IM_FONT_REGULAR_35 = loadImFont("/assets/base/fonts/chinese/simplified/ChillRoundGothic_Regular.otf", 35, true);
                IM_CONTROL_DEFAULT = loadImFont("/assets/base/fonts/chinese/simplified/ChillRoundGothic_Regular.otf", 17, true);
                break;
            case "TW":
                ImGui.getIO().setFontDefault(loadImFont("/assets/base/fonts/chinese/traditional/NotoSansTC-Regular.ttf", 17, true));
                DEFAULT = NAME_TO_FONTS.get("NotoSansTCRegular");
                SEMI_BOLD = NAME_TO_FONTS.get("NotoSansTCSemiBold");
                IM_FONT_SEMI_BOLD_20 = loadImFont("/assets/base/fonts/chinese/traditional/NotoSansTC-SemiBold.ttf", 20, true);
                IM_FONT_SEMI_BOLD_30 = loadImFont("/assets/base/fonts/chinese/traditional/NotoSansTC-SemiBold.ttf", 30, true);
                IM_FONT_REGULAR_35 = loadImFont("/assets/base/fonts/chinese/traditional/NotoSansTC-Regular.ttf", 35, true);
                IM_CONTROL_DEFAULT = loadImFont("/assets/base/fonts/chinese/traditional/NotoSansTC-Regular.ttf", 17, true);
                break;
            case "US":
            default:
                ImGui.getIO().setFontDefault(loadImFont("/assets/base/fonts/global/NotoSans-Regular.ttf", 17, false));
                DEFAULT = NAME_TO_FONTS.get("NotoSansRegular");
                SEMI_BOLD = NAME_TO_FONTS.get("NotoSansSemiBold");
                IM_FONT_SEMI_BOLD_20 = loadImFont("/assets/base/fonts/global/NotoSans-SemiBold.ttf", 20, false);
                IM_FONT_SEMI_BOLD_30 = loadImFont("/assets/base/fonts/global/NotoSans-SemiBold.ttf", 30, false);
                IM_FONT_REGULAR_35 = loadImFont("/assets/base/fonts/global/NotoSans-Regular.ttf", 35, false);
                IM_CONTROL_DEFAULT = loadImFont("/assets/base/fonts/global/NotoSans-Regular.ttf", 17, true);
                break;
        }

//        ImGui.getIO().setFontDefault(loadImFont("/assets/base/fonts/global/NotoSans-Regular.ttf", 17, false));
//
//        DEFAULT = NAME_TO_FONTS.get("NotoSansRegular");
//        SEMI_BOLD = NAME_TO_FONTS.get("NotoSansSemiBold");
//
//        IM_FONT_SEMI_BOLD_20 = loadImFont("/assets/base/fonts/global/NotoSans-SemiBold.ttf", 20, false);
//        IM_FONT_SEMI_BOLD_30 = loadImFont("/assets/base/fonts/global/NotoSans-SemiBold.ttf", 30, false);
//        IM_FONT_REGULAR_35 = loadImFont("/assets/base/fonts/global/NotoSans-Regular.ttf", 35, false);
        CHILLGOTHIC_17 = loadImFont("/assets/base/fonts/chinese/simplified/ChillRoundGothic_Regular.otf", 17, true);
    }

    public static Font loadSpecificFont(Scenario scenario, FileInfo font) {
        final byte[] fontData = (byte[]) Base.instance().assetsManager().assets(scenario.getName(), font).asset();
        return new FreeTypeFont(fontData, 65);
    }

    private static void loadFont(String name, String font) {
        try {
            final byte[] fontData = FontUtils.class.getResourceAsStream(font).readAllBytes();
            NAME_TO_FONTS.put(name, new FreeTypeFont(fontData, 65));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private static ImFont loadImFont(String font, int size, boolean full) {
        final byte[] fontData = FontUtils.class.getResourceAsStream(font).readAllBytes();

        final ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder();
        rangesBuilder.addRanges(ImGui.getIO().getFonts().getGlyphRangesDefault());
        // todo: Change the Range, because some fonts' range is only for corresponding Languages.
        if (full) {
            rangesBuilder.addRanges(ImGui.getIO().getFonts().getGlyphRangesJapanese());
            rangesBuilder.addRanges(ImGui.getIO().getFonts().getGlyphRangesChineseFull());
            rangesBuilder.addRanges(ImGui.getIO().getFonts().getGlyphRangesKorean());
        }
        final ImGuiIO data = ImGui.getIO();
        return data.getFonts().addFontFromMemoryTTF(fontData, size, new ImFontConfig(), rangesBuilder.buildRanges());
    }
}