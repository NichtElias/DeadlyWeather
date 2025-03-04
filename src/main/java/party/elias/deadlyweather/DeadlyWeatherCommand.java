package party.elias.deadlyweather;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.server.command.EnumArgument;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class DeadlyWeatherCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("deadlyweather")
                        .requires(cmdSrcStack -> cmdSrcStack.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(
                                Commands.literal("set")
                                        .then(
                                                Commands.literal("bool")
                                                        .then(
                                                                Commands.argument("key", EnumArgument.enumArgument(WeatherSettings.BoolSettings.class))
                                                                        .then(
                                                                                Commands.argument("value", BoolArgumentType.bool())
                                                                                        .executes(context -> set(context, SettingType.BOOL))
                                                                        )
                                                        )
                                        )
                                        .then(
                                                Commands.literal("int")
                                                        .then(
                                                                Commands.argument("key", EnumArgument.enumArgument(WeatherSettings.IntSettings.class))
                                                                        .then(
                                                                                Commands.argument("value", IntegerArgumentType.integer())
                                                                                        .executes(context -> set(context, SettingType.INT))
                                                                        )
                                                        )
                                        )
                                        .then(
                                                Commands.literal("double")
                                                        .then(
                                                                Commands.argument("key", EnumArgument.enumArgument(WeatherSettings.DoubleSettings.class))
                                                                        .then(
                                                                                Commands.argument("value", DoubleArgumentType.doubleArg())
                                                                                        .executes(context -> set(context, SettingType.DOUBLE))
                                                                        )
                                                        )
                                        )
                        )
                        .then(
                                Commands.literal("get")
                                        .then(
                                                Commands.literal("bool")
                                                        .then(
                                                                Commands.argument("key", EnumArgument.enumArgument(WeatherSettings.BoolSettings.class))
                                                                        .executes(context -> get(context, SettingType.BOOL))
                                                        )
                                        )
                                        .then(
                                                Commands.literal("int")
                                                        .then(
                                                                Commands.argument("key", EnumArgument.enumArgument(WeatherSettings.IntSettings.class))
                                                                        .executes(context -> get(context, SettingType.INT))
                                                        )
                                        )
                                        .then(
                                                Commands.literal("double")
                                                        .then(
                                                                Commands.argument("key", EnumArgument.enumArgument(WeatherSettings.DoubleSettings.class))
                                                                        .executes(context -> get(context, SettingType.DOUBLE))
                                                        )
                                        )

                        )
        );
    }

    private static int set(CommandContext<CommandSourceStack> context, SettingType type) {

        WeatherSettingsSD settings = WeatherSettingsSD.from(context.getSource().getLevel());

        if (type == SettingType.BOOL) {
            WeatherSettings.BoolSettings key = context.getArgument("key", WeatherSettings.BoolSettings.class);
            settings.set(key, context.getArgument("value", Boolean.class));

        } else if (type == SettingType.INT) {
            WeatherSettings.IntSettings key = context.getArgument("key", WeatherSettings.IntSettings.class);
            settings.set(key, context.getArgument("value", Integer.class));

        } else if (type == SettingType.DOUBLE) {
            WeatherSettings.DoubleSettings key = context.getArgument("key", WeatherSettings.DoubleSettings.class);
            settings.set(key, context.getArgument("value", Double.class));

        }

        settings.setDirty();

        return 1;
    }

    private static int get(CommandContext<CommandSourceStack> context, SettingType type) {

        WeatherSettingsSD settings = WeatherSettingsSD.from(context.getSource().getLevel());

        if (type == SettingType.BOOL) {
            WeatherSettings.BoolSettings key = context.getArgument("key", WeatherSettings.BoolSettings.class);
            context.getSource().sendSystemMessage(Component.literal(String.valueOf(settings.get(key))));

        } else if (type == SettingType.INT) {
            WeatherSettings.IntSettings key = context.getArgument("key", WeatherSettings.IntSettings.class);
            context.getSource().sendSystemMessage(Component.literal(String.valueOf(settings.get(key))));

        } else if (type == SettingType.DOUBLE) {
            WeatherSettings.DoubleSettings key = context.getArgument("key", WeatherSettings.DoubleSettings.class);
            context.getSource().sendSystemMessage(Component.literal(String.valueOf(settings.get(key))));
        }

        return 1;
    }

    private enum SettingType {
        BOOL,
        INT,
        DOUBLE
    }

    private static class DWSettingsKeyArgument implements ArgumentType<String> {

        @Override
        public String parse(StringReader reader) throws CommandSyntaxException {
            return reader.readUnquotedString();
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return SharedSuggestionProvider.suggest(new ArrayList<>(), builder);
        }
    }
}
