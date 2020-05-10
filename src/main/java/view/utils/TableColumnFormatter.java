package view.utils;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import model.Deadline;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TableColumnFormatter {

    public static void formatDate (TableColumn column){
        column.setCellFactory( col -> {
            TableCell<Object, LocalDate> cell = new TableCell<Object, LocalDate>() {
                private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty)
                        setText(null);
                    else
                        setText(item.format(format));
                }

            };
            return cell;
        });

    }
}
