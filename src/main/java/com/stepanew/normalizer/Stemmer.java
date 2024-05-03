package com.stepanew.normalizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Stemmer {

    private static final Pattern RV_REGION = Pattern.compile("^(.*?[аеиоуыэюя])(.*)$");
    private static final Pattern R1_REGION = Pattern.compile("^.*?[аеиоуыэюя][^аеиоуыэюя](.*)$");
    private static final Pattern PERFECTIVE_GERUND = Pattern.compile("((ив|ивши|ившись|ыв|ывши|ывшись)|" +
            "((?<=[ая])(в|вши|вшись)))$");
    private static final Pattern REFLEXIVE = Pattern.compile("(с[яь])$");
    private static final Pattern ADJECTIVE = Pattern.compile("(ее|ие|ые|ое|ими|ыми|ей|ий|ый|ой|ем|им|ым|ом|его" +
            "|ого|ему|ому|их|ых|ую|юю|ая|яя|ою|ею)$");
    private static final Pattern VERB = Pattern.compile("((ила|ыла|ена|ейте|уйте|ите|или|ыли|ей|уй|ил|ыл|им|" +
            "ым|ен|ило|ыло|ено|ят|ует|уют|ит|ыт|ены|ить|ыть|ишь|ую|ю)|((?<=[ая])(ла|на|ете|йте|ли|й|л|ем|н|ло|но|ет|" +
            "ют|ны|ть|ешь|нно)))$");
    private static final Pattern NOUN = Pattern.compile("(а|ев|ов|ие|ье|е|иями|ями|ами|еи|ии|и|ией|ей|ой|ий|й" +
            "|иям|ям|ием|ем|ам|ом|о|у|ах|иях|ях|ы|ь|ию|ью|ю|ия|ья|я)$");
    private static final Pattern SUPERLATIVE = Pattern.compile("(ейш|ейше)$");
    private static final Pattern DERIVATIONAL = Pattern.compile("(ост|ость)$");
    private static final Pattern I_PATTER = Pattern.compile("и$");
    private static final Pattern NN_PATTERN = Pattern.compile("н(н)$");
    private static final Pattern P_PATTERN = Pattern.compile("ь$");
    private static final Pattern[] FIRST_STEP_PATTERNS = {PERFECTIVE_GERUND, REFLEXIVE, ADJECTIVE, VERB, NOUN};

    //выделяем RV регион - группу символов после первой гласной
    public String rv(String word) {
        Matcher matcher = RV_REGION.matcher(word);
        if (matcher.find()) {
            return matcher.group(2);
        } else {
            return "";
        }
    }

    //выделяем PreRV регион - группу символов RV региона
    public String preRv(String word) {
        Matcher matcher = RV_REGION.matcher(word);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }

    //выделяем R1 регион - группу символов после первой согласной после RV
    public String r1(String word) {
        Matcher matcher = R1_REGION.matcher(word);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }

    //выделяем R2 регион - R1, регион в R1 регионе
    public String r2(String word) {
        String r1Part = r1(word);
        if (!r1Part.isEmpty()) {
            return r1(r1Part);
        } else {
            return null;
        }
    }

    //удаление группы символов согласно паттерну
    public String del(String word, Pattern pattern) {
        return pattern.matcher(word)
                .replaceFirst("");
    }

    public String step1(String word) {
        for (Pattern pattern : FIRST_STEP_PATTERNS) {
            if (pattern.matcher(word).find()) {
                return del(word, pattern);
            }
        }

        return word;
    }

    public String step2(String word) {
        return I_PATTER.matcher(word).replaceFirst("");
    }

    public String step3(String word) {
        String r2Part = r2(word);
        if (r2Part != null && DERIVATIONAL.matcher(r2Part).find()) {
            return del(word, DERIVATIONAL);
        } else {
            return word;
        }
    }

    public String step4a(String word) {
        return NN_PATTERN.matcher(word).find() ? NN_PATTERN.matcher(word).replaceAll("н") : word;
    }

    public String step4b(String word) {
        return SUPERLATIVE.matcher(word).find() ? step4a(SUPERLATIVE.matcher(word).replaceAll("")) : word;
    }

    public String step4c(String word) {
        return P_PATTERN.matcher(word).find() ? P_PATTERN.matcher(word).replaceAll("") : word;
    }

    public String stem(String word) {
        String preRvPart = preRv(word);
        String rvPart = rv(word);

        if (rvPart.isEmpty()) {
            return word;
        }

        return preRvPart + step4c(step4b(step4a(step3(step2(step1(rvPart))))));
    }

}
