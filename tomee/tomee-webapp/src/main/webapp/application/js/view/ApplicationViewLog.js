/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

TOMEE.ApplicationViewLog = function (cfg) {
    "use strict";

    var channel = cfg.channel;

    var panel = TOMEE.components.Panel({
        title:'-',
        bbar:[
            {
                elName:'fileSelector',
                tag:'select',
                attributes:{
                    style:'margin-right: 2px;'
                }
            },
            {
                elName:'loadBtn',
                tag:'button',
                cls:'btn',
                html:TOMEE.I18N.get('application.log.load'),
                listeners:{
                    'click':function () {
                        var file = panel.getElement('fileSelector').val();
                        var tail = 100; //TODO
                        channel.send('trigger.log.load', {
                            file:file,
                            tail:tail
                        });
                        panel.setTitle(file);
                    }
                }
            }
        ]
    });

    var loadFilesField = function (files) {
        var getOption = function (fileName) {
            var option = $('<option></option>');
            option.attr('value', fileName);
            option.append(fileName);
            return option;
        };

        var selector = panel.getElement('fileSelector');
        selector.empty();
        if (!files) {
            return;
        }
        for (var i = 0; i < files.length; i++) {
            selector.append(getOption(files[i]));
        }
    };

    var loadLogTable = function (data) {
        if (!data || !data.lines) {
            return;
        }

        (function (lines) {
            panel.getContentEl().empty();

            var newData = $('<div></div>');
            for (var i = 0; i < lines.length; i++) {
                newData.append(lines[i]);
                newData.append('<br/>');
            }

            panel.getContentEl().append(newData);
            //innerPanel.scrollTop(newData.height());
            panel.getContentEl().animate({
                scrollTop:newData.height()
            }, 500);
        })(data.lines);

        panel.getElement('fileSelector').val(data.name);
    };

    var loadData = function (data) {
        loadFilesField(data.files);
        loadLogTable(data.log);
    };

    var wrapper = $('<div style="margin: 5px"></div>')
    wrapper.append(panel.getEl());

    return {

        getEl:function () {
            return wrapper;
        },
        setHeight:function (height) {
            wrapper.height(height);
            var innerSize = height - TOMEE.el.getBorderSize(wrapper);
            panel.setHeight(innerSize);
        },
        loadData:loadData
    };
};