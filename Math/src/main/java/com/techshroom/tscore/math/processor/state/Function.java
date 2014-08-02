package com.techshroom.tscore.math.processor.state;

public abstract class Function<RETURN> extends OneValueStruct<RETURN> {
    private OneValueStruct<String> name;
    protected final BasicState scope = new BasicState();

    Function(OneValueStruct<String> nme, Variable<?>... args) {
        super();
        name = nme;
        scope.addVars(args);
        super_data(call());
    }

    Function(OneValueStruct<String> nme) {
        this(nme, new Variable<?>[0]);
    }

    public String name() {
        return name.data();
    }

    public Function<RETURN> name(String nme) {
        name.data(nme);
        return this;
    }

    /**
     * This call is not supported. If subclassing, use
     * {@link #super_data(RETURN)}
     */
    @Override
    public RETURN data(RETURN dta) {
        return data();
    }

    /**
     * The replacement for {@link #data(RETURN)}.
     * 
     * @param data
     *            - the data to set
     * @return the set data
     */
    protected final RETURN super_data(RETURN data) {
        return super.data(data);
    }

    protected abstract RETURN call();
}
