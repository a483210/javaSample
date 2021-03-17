package com.xy.im.base.error;

import javax.lang.model.element.Element;

/**
 * 处理异常
 *
 * @author Created by gold on 2019-09-02 15:26
 */
public class ProcessorException extends RuntimeException {

    private Element element;

    public ProcessorException(String message) {
        super(message);
    }

    public ProcessorException(Element element, String message) {
        super(message);

        this.element = element;
    }

    public Element getElement() {
        return element;
    }
}