package com.appdynamics.ace.extension.groovy.command;

import com.singularity.ee.controller.api.exceptions.ServerException;

import groovy.lang.Binding;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.Script;

import java.lang.reflect.Method;
import java.util.HashSet;

public abstract class BaseGroovyScript extends Script {


    private GroovyConsoleService _console;
    private HashSet<String> _scripts;

    public Object invokeMethod(String name, Object args) {

        if (!hasLocalMethod(name) && hasScript(name))
            return executeScript(name,args);

        else return super.invokeMethod(name,args);
    }

    private Object executeScript(String name, Object args)  {
       try {
           Script script = _console.createScript(name,args,_console.createBinding());

             return  script.run();
       } catch (Exception e) {
           throw new GroovyRuntimeException("Error while Executing Script",e);
       }
    }

    private boolean hasScript(String name) {
        return _scripts.contains(name);
    }

    private boolean hasLocalMethod(String name) {
        for (Method m : this.getClass().getMethods()) {
            if (m.getName().equals(name)) return true;
        }
        return false;
    }

    public void setConsole(GroovyConsoleService console) {
        System.out.println("XXXX: Set Console To :"+console);
        _console = console;
        _scripts = new HashSet<String>();
        _scripts.addAll(console.listScripts());
    }

    public GroovyConsoleService getConsole() {
        return _console;
    }

    @Override
    public void setBinding(Binding binding) {
        super.setBinding(binding);
        if (binding.hasVariable("_console")) setConsole((GroovyConsoleService) binding.getVariable("_console"));
    }

    public String helloWorld (String world) throws ServerException {

        return world.toUpperCase();
    }


}