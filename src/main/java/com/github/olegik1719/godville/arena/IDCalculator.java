package com.github.olegik1719.godville.arena;



public interface IDCalculator {
    /**
     * Возвращает ID из ссылки на лог(идентификатор хроники)
     * @param url -- ссылка на хронику
     * @return -- возвращает символы-индентификатор.
     */
    String getID(String url);
}
