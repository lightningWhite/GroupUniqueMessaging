package com.gum.dlt.groupuniquemessaging;

import java.util.List;
import com.gum.dlt.groupuniquemessaging.Parser;
/**
 * Created by Daniel on 10/30/2017.
 * This class contains all of the information associated with a Message.
 */

public class Message {
    public String _msgTitle;
    public List<String> _variables;
    public String _msgWithVariables;
    public String _msgWithSetVariables;
    public MsgParser _parser; // TODO: This should be declared as Parser so we can program to an interface instead of a class


    public void Message(String msgTitle, String msgWithVariables) {
        _msgTitle = msgTitle;
        _msgWithVariables = msgWithVariables;
        _parser = new MsgParser(_msgWithVariables);
    }

    public String get_msgTitle() {
        return _msgTitle;
    }

    public void set_msgTitle(String _msgTitle) {
        this._msgTitle = _msgTitle;
    }

    public List<String> get_variable_names_from_msg() {
        return _parser.getVariableNames();
    }

    public String get_msgWithVariables() {
        return _msgWithVariables;
    }

    public void set_msgWithVariables(String _msgWithVariables) {
        this._msgWithVariables = _msgWithVariables;
    }

    public String get_msgWithSetVariables() {
        _msgWithSetVariables = _parser._msgWithSetVariables;
        return _msgWithSetVariables;
    }
}
