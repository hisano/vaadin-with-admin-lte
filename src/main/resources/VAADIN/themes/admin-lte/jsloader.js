// http://labs.37to.net/javascript/jsloader/
var JSLoader = function(options){
    var options  = options || {};
    this.pointer = 0;
    this.finish  = options.finish || function(){};
    this.append  = options.append || null;
    this.queue = {
        length : 0,
        list : [],
        push : function(arg){
            this.list.push(arg);
            this.length++;
        }
    };
    return this;
};

JSLoader.prototype = {

    next : function(){
        var self = this;
        var loader = new JSLoader({
            append : self.append,
            finish : function(){
                self._next();
            }
        });
        var args = [];
        for(var i=0, l=arguments.length; i<l; i++){
            loader.assign(arguments[i]);
        }
        self.assign(function(){
            loader.run();
        });
        return this;
    },

    _next : function(){
        var func = this.queue.list[this.pointer++];
        if(func){
            func();
        }
    },

    assign : function(arg){
        var self = this;
        switch(typeof arg){
          case 'string' :
            this.queue.push(function(){self.load(arg,{
                append : self.append,
                onload : function(){
                    self.report();
                }});
            });
            break;
          case 'function' :
            this.queue.push(function(){arg();self.report();});
            break;
        }
    },

    report : function(){
        this.queue.length--;
        if(this.queue.length == 0){
            this.finish();
        }
    },

    start : function(){
        this._next();
    },

    run : function(){
        for( var i=0,l=this.queue.length; i<l; i++){
            this.queue.list[i]();
        }
    },

    load : function(src, options){
        var options   = options || {};
        var element   = options.append || document.body || document.documentElement;
        var script    = document.createElement('script');
        script.src    = src;
        script.type   = options.type   || 'text/javascript';
        script.onload = options.onload || function(){};
        if(options.charset)
          script.charset = options.charset;
        if( document.all ){
            script.onreadystatechange = function(){
                switch(script.readyState){
                  case 'complete':
                  case 'loaded' :
                    script.onload();
                    break;
                }
            };
        }
        element.appendChild(script);
    }
};



