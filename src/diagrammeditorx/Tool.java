/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diagrammeditorx;

/**
 * Типы элементов диаграммы
 */
public enum Tool {
    NONE,                   // Пустой элемент 
    TASK,                   // Процесс (задание)
    START_EVENT,            // Начальное событие
    END_EVENT,              // Конечное событие
    TIMER,                  // Таймер
    ANNOTATION,             // Аннльацтя
    POOL,                   // Пул
    EXCLUSIVE,              // Эксклюзивный шлюз (Исключающее ИЛИ)
    INCLUSIVE,              // Инклюзивный шлюз (Включающее ИЛИ)
    PARALLEL,               // Параллельный шлюз (И)
    ASSOCIATION,            // Ассоциация
    FLOW,                   // Поток управления
    START_PAGE_CONNECTOR,   // Начальный разделитель страниц
    END_PAGE_CONNECTOR      // Конечный разделитель страниц
}
