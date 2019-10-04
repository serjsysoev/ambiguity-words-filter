/*
 * Copyright (C) 2017  Alexander Porechny alex.porechny@mail.ru
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Attribution-NonCommercial-ShareAlike 3.0 Unported
 * (CC BY-SA 3.0) as published by the Creative Commons.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Attribution-NonCommercial-ShareAlike 3.0 Unported (CC BY-SA 3.0)
 * for more details.
 *
 * You should have received a copy of the Attribution-NonCommercial-ShareAlike
 * 3.0 Unported (CC BY-SA 3.0) along with this program.
 * If not, see <https://creativecommons.org/licenses/by-nc-sa/3.0/legalcode>
 *
 * Thanks to Sergey Politsyn and Katherine Politsyn for their help in the development of the library.
 *
 *
 * Copyright (C) 2017 Александр Поречный alex.porechny@mail.ru
 *
 * Эта программа свободного ПО: Вы можете распространять и / или изменять ее
 * в соответствии с условиями Attribution-NonCommercial-ShareAlike 3.0 Unported
 * (CC BY-SA 3.0), опубликованными Creative Commons.
 *
 * Эта программа распространяется в надежде, что она будет полезна,
 * но БЕЗ КАКИХ-ЛИБО ГАРАНТИЙ; без подразумеваемой гарантии
 * КОММЕРЧЕСКАЯ ПРИГОДНОСТЬ ИЛИ ПРИГОДНОСТЬ ДЛЯ ОПРЕДЕЛЕННОЙ ЦЕЛИ.
 * См. Attribution-NonCommercial-ShareAlike 3.0 Unported (CC BY-SA 3.0)
 * для более подробной информации.
 *
 * Вы должны были получить копию Attribution-NonCommercial-ShareAlike 3.0
 * Unported (CC BY-SA 3.0) вместе с этой программой.
 * Если нет, см. <https://creativecommons.org/licenses/by-nc-sa/3.0/legalcode>
 *
 * Благодарим Полицыных Сергея и Екатерину за оказание помощи в разработке библиотеки.
 */
package ru.textanalysis.tawt.awf;

import ru.textanalysis.tawt.ms.internal.sp.BearingPhraseSP;
import ru.textanalysis.tawt.ms.internal.sp.WordSP;
import ru.textanalysis.tawt.rfc.RelationshipHandler;

import java.util.List;

public class AWF {
    private RelationshipHandler relationshipHandler = new RelationshipHandler();

    /*
    возвращает true, если бы хотя бы одна связь была установлена
     */
    public void applyAwfForBearingPhrase(BearingPhraseSP bearingPhraseSP) {
        bearingPhraseSP.applyConsumer(words -> {
            establishCompatibilityForPretext(words);
            establishCompatibilityForOneForm(words);
            establishCompatibilityForOneTos(words);
        });
    }

    private void establishCompatibilityForPretext(List<WordSP> words) {
        for (int i = words.size() - 1; -1 < i; i--) {
            WordSP word = words.get(i);
            if (word.havePretext()) {
                for (int j = words.size() - 1; i < j; j--) {
                    if (relationshipHandler.establishRelationForPretext(word, words.get(j))) {
                        break;
                    }
                }
            }
        }
    }

    private void establishCompatibilityForOneForm(List<WordSP> words) {
        for (int i = words.size() - 1; -1 < i; i--) {
            WordSP word = words.get(i);
            if (word.isMonoSemantic()) {
                for (int j = words.size() - 1; i < j; j--) {
                    if (relationshipHandler.establishRelation(j - i, word, words.get(j))) {
                        //todo log;
                    }
                }
            }
        }
    }

    private void establishCompatibilityForOneTos(List<WordSP> words) {
        for (int i = words.size() - 1; -1 < i; i--) {
            WordSP word = words.get(i);
            if (word.isOneTos() && !word.isMonoSemantic()) {
                for (int j = words.size() - 1; i < j; j--) {
                    if (relationshipHandler.establishRelation(j - i, word, words.get(j))) {
                        //todo log;
                    }
                }
            }
        }
    }

    public void init() {
    }
}
