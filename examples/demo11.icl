def a = new(2) in
	def b = new(*a) in
		def c = a in
			a := *b + 2;
			c := *c + 2;
			println *a ;
			println *c
		end
	end
end ;;