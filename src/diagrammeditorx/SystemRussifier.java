package diagrammeditorx;

import javax.swing.UIManager;

/**
 * Русификатор системных диалогов с пользователем
 */
public class SystemRussifier {
    /**
     * функция русификации текстовых констант системных диалогов
     */
    static public void Russify(){
        UIManager.put("FileChooser.fileNameLabelText", "Имя файла:");
        UIManager.put("FileChooser.acceptAllFileFilterText", "Все файлы");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        UIManager.put("FileChooser.deleteFileButtonText", "Удалить");
        UIManager.put("FileChooser.deleteFileButtonToolTipText", "Удалить файл");
        UIManager.put("FileChooser.detailsViewButtonAccessibleName", "Подробно");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Подробно");
        UIManager.put("FileChooser.directoryDescriptionText", "Папка");
        UIManager.put("FileChooser.directoryOpenButtonText", "Открыть");
        UIManager.put("FileChooser.directoryOpenButtonToolTipText", "Открыть");
        UIManager.put("FileChooser.enterFilenameLabelText", "Имя");
        UIManager.put("FileChooser.fileDescriptionText", "Описание");
        UIManager.put("FileChooser.filesLabelText", "Файлы");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Типы файлов:");
        UIManager.put("FileChooser.filterLabelText", "Тип(ы) файла");
        UIManager.put("FileChooser.foldersLabelText", "Папка");
        UIManager.put("FileChooser.helpButtonText", "Помощь");
        UIManager.put("FileChooser.helpButtonToolTipText", "Помощь");
        UIManager.put("FileChooser.homeFolderAccessibleName", "Дом");
        UIManager.put("FileChooser.homeFolderToolTipText", "Дом");
        UIManager.put("FileChooser.listViewButtonAccessibleName", "Список");
        UIManager.put("FileChooser.listViewButtonToolTipText", "Список");
        UIManager.put("FileChooser.lookInLabelText", "Католог:");
        UIManager.put("FileChooser.newFolderAccessibleName", "Создать папку");
        UIManager.put("FileChooser.newFolderButtonText", "Создать папку");
        UIManager.put("FileChooser.newFolderButtonToolTipText", "Создать папку");
        UIManager.put("FileChooser.newFolderDialogText", "Создать папку");
        UIManager.put("FileChooser.newFolderErrorSeparator", "Ошибка создания");
        UIManager.put("FileChooser.newFolderErrorText", "Ошибка создания");
        UIManager.put("FileChooser.newFolderToolTipText", "Создать папку");
        UIManager.put("FileChooser.openButtonText", "Открыть");
        UIManager.put("FileChooser.openButtonToolTipText", "Открыть");
        UIManager.put("FileChooser.openDialogTitleText", "Открыть");
        UIManager.put("FileChooser.other.newFolder", "Создать папку");
        UIManager.put("FileChooser.other.newFolder.subsequent", "Создать папку");
        UIManager.put("FileChooser.win32.newFolder", "Создать папку");
        UIManager.put("FileChooser.win32.newFolder.subsequent", "Создать папку");
        UIManager.put("FileChooser.pathLabelText", "Путь");
        UIManager.put("FileChooser.renameFileButtonText", "Переименовать");
        UIManager.put("FileChooser.renameFileButtonToolTipText", "Переименовать");
        UIManager.put("FileChooser.renameFileErrorText", "Ошибка переименования");
        UIManager.put("FileChooser.renameFileErrorTitle", "Ошибка переименования");
        UIManager.put("FileChooser.saveButtonText", "Сохранить");
        UIManager.put("FileChooser.saveButtonToolTipText", "Сохранить");
        UIManager.put("FileChooser.saveDialogTitleText", "Сохранить");
        UIManager.put("FileChooser.saveInLabelText", "Католог:");
        UIManager.put("FileChooser.updateButtonText", "Обновить");
        UIManager.put("FileChooser.updateButtonToolTipText", "Обновить");
        UIManager.put("FileChooser.upFolderAccessibleName", "Вверх");
        UIManager.put("FileChooser.upFolderToolTipText", "Вверх");
        UIManager.put("FileChooser.desktopFolderToolTipText", "Рабочий стол");
    }
}
