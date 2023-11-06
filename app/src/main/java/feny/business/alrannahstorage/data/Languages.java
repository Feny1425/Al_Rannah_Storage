package feny.business.alrannahstorage.data;

import java.util.Arrays;
import java.util.List;

public class Languages {
    public static List<String> WORDS = Arrays.asList(
            "two years",
            "years",
            "year",
            "two days",
            "days",
            "day",
            "two months",
            "months",
            "month",
            "before",
            "ago",
            "enough",
            "and",
            "just",
            "now"
    );

    public static class Arabic{
        public static List<String> WORDS = Arrays.asList(
                "سنتين",
                "سنوات",
                "سنة",
                "يومين",
                "أيام",
                "يوم",
                "شهرين",
                "شهور",
                "شهر",
                "قبل",
                "مرت",
                "كفاية",
                "و",
                "فقط",
                "الآن"
        );
        public static String EN_TO_AR(String text){
            String _text = text;
            for(int i = 0; i < Languages.WORDS.size(); i++){
                _text = _text.replace(Languages.WORDS.get(i),WORDS.get(i));
            }
            return _text;
        }
    }
}