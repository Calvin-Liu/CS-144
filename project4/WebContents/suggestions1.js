function SuggestionProvider() {
    //any initializations needed go here
}

SuggestionProvider.prototype.requestSuggestions = function (oAutoSuggestControl) {

    var aSuggestions = new Array();

    //determine suggestions for the control
    oAutoSuggestControl.autosuggest(aSuggestions);
};

