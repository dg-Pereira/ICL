 def x = 1 in 
	def f = fun y:Int -> y+x end in
		def g = fun x:Int -> x + f(x) end
			in print g(2)
		end
	end
end ;;