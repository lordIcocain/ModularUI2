package com.cleanroommc.modularui.api.drawable;

import com.cleanroommc.modularui.api.math.Color;
import com.cleanroommc.modularui.api.math.Pos2d;
import com.cleanroommc.modularui.api.math.Size;
import com.cleanroommc.modularui.common.internal.JsonHelper;
import com.cleanroommc.modularui.common.widget.TextWidget;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

public class Text implements IDrawable {

    private final Supplier<String> textSupplier;
    private final String text;
    @Nullable
    private Supplier<Object[]> localisationData;
    private boolean dynamicLocalisation = false;
    private int color = 0x212121;
    private boolean shadow = false;

    private Text(String text, Supplier<String> textSupplier) {
        this.text = text;
        this.textSupplier = textSupplier;
    }

    public static Text dynamic(Supplier<String> textSupplier) {
        return new Text(null, Objects.requireNonNull(textSupplier, "TextSupplier can't be null!"));
    }

    public Text(String text) {
        this(Objects.requireNonNull(text, "String in Text can't be null!"), null);
    }

    public static Text of(ITextComponent textComponent) {
        return new Text(textComponent.getFormattedText());
    }

    public Text color(int color) {
        this.color = color;
        return this;
    }

    public Text shadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    public Text localise(Supplier<Object[]> localisationData) {
        this.localisationData = localisationData;
        this.dynamicLocalisation = true;
        return this;
    }

    public Text localise(Object... localisationData) {
        localise(() -> localisationData);
        this.dynamicLocalisation = false;
        return this;
    }

    public Text shadow() {
        return shadow(true);
    }

    public int getColor() {
        return color;
    }

    public boolean hasShadow() {
        return shadow;
    }

    public String getRawText() {
        return textSupplier == null ? text : textSupplier.get();
    }

    @Override
    public void draw(Pos2d pos, Size size, float partialTicks) {
        TextRenderer.drawString(getFormatted(), pos, color, size.width);
    }

    public String getFormatted() {
        String text = getRawText();
        if (localisationData != null && FMLCommonHandler.instance().getSide().isClient()) {
            text = I18n.format(text, localisationData.get());
        }
        text = TextRenderer.getColorFormatString(color) + text;
        if (hasShadow()) {
            text += TextRenderer.FORMAT_CHAR + 's';
        }
        return text;
    }

    public static String getFormatted(Text... texts) {
        StringBuilder builder = new StringBuilder();
        for (Text text : texts) {
            builder.append(text.getFormatted());
        }
        return builder.toString();
    }

    public boolean isDynamic() {
        return textSupplier != null || dynamicLocalisation;
    }

    public static Text ofJson(JsonElement json) {
        if (json.isJsonObject()) {
            JsonObject jsonObject = json.getAsJsonObject();
            Text text = new Text(JsonHelper.getString(jsonObject, "E:404", "text"));
            text.shadow(JsonHelper.getBoolean(jsonObject, false, "shadow"));
            Integer color = JsonHelper.getElement(jsonObject, null, Color::ofJson, "color");
            if (color != null) {
                text.color(color);
            }
            if (JsonHelper.getBoolean(jsonObject, false, "localise")) {
                text.localise();
            }
            return text;
        }
        if (!json.isJsonPrimitive() && !json.isJsonArray()) {
            return new Text(json.getAsString());
        }
        return new Text("");
    }
}
