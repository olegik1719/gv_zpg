package com.github.olegik1719.godville.duels;



public interface IDCalculator {
    /**
     * Возвращает ID из ссылки на лог(идентификатор хроники)
     * @param url -- ссылка на хронику
     * @return -- возвращает символы-индентификатор.
     */
    static String getID(String url){return url;}
}
