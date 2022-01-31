def f = fun x:Int -> x+1 end in
	def g = fun y:Int -> f(y)+2 end in
		def x = g(2) in
			x+x
		end
	end
end ;;