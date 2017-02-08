(function () {
	'use strict';

	angular
	.module('app')
	.factory('autoComplete', function ($http, $q) {

		return {

			SearchBarAutoComplete: function(str) {

				var reponse;
				return $http({
					method: 'POST',
					url: 'http://ws.audioscrobbler.com/2.0/',
					data: 'method=track.search&' +
					'track='+str+"&"+
					'api_key=5bf9260e6a06dd0e62142e1185ba4e69&' +
					'limit=5&'+
					'format=json'
				}).then(function(response) {
					return (response.data.results.trackmatches.track);
				}, function(response) {
					console.log("ca bug ici");
					return $q.reject(response.data.results.trackmatches.track);
				});


			}

		};

	});
})();