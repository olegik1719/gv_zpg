package com.github.olegik1719.godville.arena;

public interface ChronicGetter {

    /** Возвращает хронику в виде HTML-страницы
     * Различные варианты того, откуда он их берет:
     * Файл на диске, или страница в интернете
     * Или вообще -- запись в БД.
     * @param chronicleID -- ИД хроники;
     * @return HTML страницы или пустую строку
     */
    String getHtml(String chronicleID);
}
